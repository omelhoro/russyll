(ns russyll.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reagent.core :as r]
            [text]
            [re-frame.core :as re-frame]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType])

  (:use
   [globals :only [sformat]]
   [syllab :only [syll-single]]
   [orphoep :only [orpho-single ++ --]]
   [usertest :only [serve-words-rand calc-stats]]
   )
  (:import goog.History)
  )

                                        ; DB

(def example-text (str
                   "На вокз'але Никол'аевской жел'езной дор'оги встр'етились дв'а при'ятеля: од'ин т'олстый, друг'ой т'онкий. Т'олстый т'олько что пооб'едал на вокз'але, и г'убы ег'о, подёрнутые м'аслом, лосн'ились, как сп'елые в'ишни. П'ахло от нег'о х'ересом и флёр-д’ор'анжем."
                   "\nТ'онкий же т'олько что в'ышел из ваг'она и б'ыл навь'ючен чемод'анами, узл'ами и карт'онками. П'ахло от нег'о ветчин'ой и коф'ейной г'ущей. 'Из-за ег'о спин'ы выгл'ядывала х'уденькая ж'енщина с дл'инным подбор'одком - ег'о жен'а, и выс'окий гимназ'ист"
                   "с прищ'уренным гл'азом - ег'о с'ын. - Порф'ирий! - воскл'икнул т'олстый, ув'идев т'онкого. - Т'ы ли 'это? Гол'убчик м'ой! Ск'олько з'им, ск'олько л'ет!"))

(def user-history (r/atom {}))
(def how-many 10)
(def app-db
  {
   :user-history {}
   :user-progress 0
   :result  [0 0 0 0 0]
   :current-word "Igor"
   :text-in example-text
   :text-syllabied ""
   :table-syllabied {}
   :splitted-mapped-atom {}
   :current-model "0"
   :splitted=mapped []
   :done false
   })

;; Subscriptions

(re-frame/reg-sub
 :key-in-db
 (fn [db [_ key]] (get db key)))

;; Handlers

;; -- Self test
(re-frame/reg-event-db
 :initiliaze
 (fn [_ _] app-db))

(re-frame/reg-event-db
 :set-current-word
 (fn [db [_ word]] (assoc db :current-word word)))

(defn choose-word [user-history]
  (let [choice (serve-words-rand)]
    (if (contains? user-history choice)
      (choose-word user-history)
      choice)))

(defn set-new-word [db _]
  (let [
        user-history (:user-history db)
        choice (choose-word user-history)
        ]
    (-> db
        (assoc-in [:user-history choice] choice)
        (assoc :current-word choice))
    ))

(defn end-or-continue [db how-many how-many-guessed]
  (if (>= how-many-guessed how-many)
    (-> db
        (assoc :result (calc-stats (:user-history db)))
        (assoc :done true)
        )
    (set-new-word db [])))

(defn debug [db]
  (do (print db) db))

(re-frame/reg-event-db
 :next-word
 (fn [db [_ word]]
   (let [current-word (:current-word db)
         n-of-separations (->> current-word (filter #(= \- %)) (count))
         k (clojure.string/replace current-word "-" "")
         user-history (:user-history db)
         how-many-guessed (-> user-history (count))
         ]
     (if (= n-of-separations 1)
       (-> db
           (assoc-in [:user-history k] current-word)
           (assoc :user-progress (* how-many-guessed (/ 100 how-many)))
           (end-or-continue how-many how-many-guessed)
                  )
       (do
         (js/alert (str "Dashlines for separations should be 1, found: " n-of-separations))
         db)))))

(re-frame/reg-event-db
 :choose-word
 set-new-word)

;; -- Text syllabification

(re-frame/reg-event-db
 :syllaby
 (fn [db _]
   (let [
         text-val (:text-in db)
         splitted-t (text/tokenize text-val "'")
         map-words (text/set-of-vals splitted-t)
         current-model (:current-model db)
         text-done (++ (text/text-by-model splitted-t map-words current-model))
         splitted-mapped (map #(vector % (get map-words %)) splitted-t)
         ]
     (-> db
         (assoc :splitted=mapped splitted-mapped)
         (assoc :table-syllabied map-words)
         (assoc :text-syllabied text-done))
     )))

(re-frame/reg-event-db
 :change-model
 (fn [db [_ model]]
   (assoc db :current-model model)))

(re-frame/reg-event-db
 :change-text
 (fn [db [_ text]]
   (assoc db :text-in text)))

;; -------------------------
;; Views

(defn progress-tab []
  (let [user-progress (re-frame/subscribe [:key-in-db :user-progress])]
    (fn []
      [:div.progress
       {:style {:margin-top "10px"}}
       [:div.progress-bar.progress-bar-success {:style {:width (str @user-progress "%")}}]])))

(def textarea-style {:width "100%" :height 200})

(def models
  [
   {:i 0 :name "Vinogradov/Sherba (SM1)" :short "SM1"}
   {:i 1 :name "Avanesov (SM2)" :short "SM2"}
   {:i 2 :name "Bondarko (SM3)" :short "SM3"}
   {:i 3 :name "Kempgen (SM4)" :short "SM4"}
   {:i 4 :name "SSP" :short "SM5"}
   ])

(defn textarea []
  (let [
        table (re-frame/subscribe [:key-in-db :table-syllabied])
        current-model (re-frame/subscribe [:key-in-db :current-model])
        text-in (re-frame/subscribe [:key-in-db :text-in])
        text-syllabied (re-frame/subscribe [:key-in-db :text-syllabied])
        splitted-mapped (re-frame/subscribe [:key-in-db :splitted=mapped])
        ]
    (fn []
      [:div
       [:h2.ribbon (str "Put in some data!")]
       [:select.form-control
        {:on-change (fn [evt] (do (re-frame/dispatch [:change-model (-> evt .-target .-value)]) (re-frame/dispatch [:syllaby @text-in]))) :value @current-model}
        (for [{:keys [name i]} models]
          [:option {:value i :key i} name])
        ]
       [:textarea
        {:style textarea-style
         :on-change (fn [evt] (do (re-frame/dispatch [:change-text (-> evt .-target .-value)]) (re-frame/dispatch [:syllaby]))) :value @text-in}]
       [:pre @text-syllabied]
       [:table.table.table-striped.table-bordered.table-hover.table-condensed.syllabied
        [:tr [:th] (for [{:keys [short name]} models] [:th {:key name :title name} short])]
        (for
            [[word syll-models] @splitted-mapped]
          [:tr {:key (rand)} [:td word]
           (map-indexed (fn [ix word] [:td {:key ix} word]) syll-models)
           ]
          )
        ]
       ]
      ))
  )

(defn table-text []
  [:div]
  )

(defn check-yourself []
  (let [
        current-word (re-frame/subscribe [:key-in-db :current-word])
        user-history (re-frame/subscribe [:key-in-db :user-history])
        result (re-frame/subscribe [:key-in-db :result])
        done (re-frame/subscribe [:key-in-db :done])
        ]
    (fn []
      [:div
       [:h2.ribbon "Your turn!"]
       [:h4 "Here you can check yourself: Which model follows your intution?"]
       [:p.well.bg-warning
        [:strong "Important: "]
        "Set the division with a dash '-': 'игорь' -> 'и-горь'. There will be 10 words to divide."
        [:div.input-group.col-sm-4
         [:input.form-control {:disabled @done :value @current-word, :on-change #(re-frame/dispatch [:set-current-word (-> % .-target .-value)])}]
         [:div.input-group-btn
          [:button.btn.btn-default
           {:disabled @done :on-click #(re-frame/dispatch [:next-word])} (if (-> (count @user-history) (= how-many)) "Result" "Next")]]]
        [:ul.list-group
         {:style {:margin-top "10px"}}
         (for [[i [k v]] (zipmap (-> @user-history (count) (range)) @user-history)]
           [:li.list-group-item {:key k} (str (inc i) ". " k " -> " v)])]]
       [:table.table
        [:tr
         (for [{:keys [short]} models] [:th {:key short}  short])] [:tr (for [v @result] [:td {:key (rand)} v])]]
       ((progress-tab))
       ])))


(defn home-page []
  [:div [:h1.header.navbar "Welcome to " [:strong "russyll"] ", the automatic syllable divider"]
   [:div#app-body
    ((check-yourself))
    ((textarea))
    (table-text)
    ]
   ]
  )

(defn current-page []
  [:div [(session/get :current-page)]]
  )

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (re-frame/dispatch [:initiliaze])
  (re-frame/dispatch [:choose-word])
  (re-frame/dispatch [:change-text example-text])
  (re-frame/dispatch [:syllaby])
  (mount-root)
  )

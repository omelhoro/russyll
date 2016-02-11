(ns russyll.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reagent.core :as r]
            [text]
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

;; -------------------------
;; Views

(def user-history (r/atom {}))

(def user-progress (r/atom (str 0 "%")))

(def result (r/atom [0 0 0 0 0]))

(def how-many 10)

(defn choose-word []
  (let [choice (serve-words-rand)]
    (if (contains? @user-history choice) (choose-word) (do (swap! user-history assoc choice choice) choice))))

(def current-word (r/atom (choose-word)))

(defn progress-tab []
  [:div.progress
   {:style {:margin-top "10px"}}
   [:div.progress-bar.progress-bar-success {:style {:width @user-progress}}]])

(defn next-test []
  (let [input @current-word n-of-separations (count (filter #(= \- %) input)) k (clojure.string/replace input "-" "")]
    (if (= n-of-separations 1)
      (do
        (swap! user-history assoc k input)
        (swap! user-progress #(-> (count @user-history) (* (/ 100 how-many)) (str "%")))
        (print @user-history)
        (if (-> (count @user-history) (= how-many))
          (swap! result #(calc-stats @user-history))
          (swap! current-word choose-word)))
      (js/alert "Please set a dash to separate syllables!"))))

(def textarea-style {:width "100%" :height 200})

(def text-in
  (r/atom
    (str "На вокз'але Никол'аевской жел'езной дор'оги встр'етились дв'а при'ятеля: од'ин т'олстый, друг'ой т'онкий. Т'олстый т'олько что пооб'едал на вокз'але, и г'убы ег'о, подёрнутые м'аслом, лосн'ились, как сп'елые в'ишни. П'ахло от нег'о х'ересом и флёр-д’ор'анжем."
         "\nТ'онкий же т'олько что в'ышел из ваг'она и б'ыл навь'ючен чемод'анами, узл'ами и карт'онками. П'ахло от нег'о ветчин'ой и коф'ейной г'ущей. 'Из-за ег'о спин'ы выгл'ядывала х'уденькая ж'енщина с дл'инным подбор'одком - ег'о жен'а, и выс'окий гимназ'ист"
         "с прищ'уренным гл'азом - ег'о с'ын. - Порф'ирий! - воскл'икнул т'олстый, ув'идев т'онкого. - Т'ы ли 'это? Гол'убчик м'ой! Ск'олько з'им, ск'олько л'ет!")))

(def text-syllabied
  (r/atom ""))

(def table-syllabied (r/atom {}))

(def splitted-mapped-atom (r/atom []))

(def current-model (r/atom 0))

(defn syllaby [text-val]
  (let [
        splitted-t (text/tokenize text-val "'")
        map-words (text/set-of-vals splitted-t)
        text-done (++ (text/text-by-model splitted-t map-words @current-model))
        splitted-mapped (map #(vector % (get map-words %)) splitted-t)
        ]
    (do
      (swap! splitted-mapped-atom #(-> splitted-mapped))
      (swap! table-syllabied #(-> map-words))
      (swap! text-syllabied #(-> text-done))
      )
    ))

(def models
  [
   {:i 0 :name "Vinogradov/Sherba (SM1)" :short "SM1"}
   {:i 1 :name "Avanesov (SM2)" :short "SM2"}
   {:i 2 :name "Bondarko (SM3)" :short "SM3"}
   {:i 3 :name "Kempgen (SM4" :short "SM4"}
   {:i 4 :name "SSP" :short "SM5"}
   ])

(defn textarea []
  (let [table @table-syllabied]
    [:div
     [:h2.ribbon "Put in some data!"]
     [:select.form-control {:on-change (fn [evt] (do (swap! current-model #(-> evt .-target .-value)) (syllaby @text-in))) :value @current-model}
      (for [{:keys [name i]} models]
        [:option {:value i :key i} name])
      ]
     [:textarea {:style textarea-style :on-change (fn [evt] (do (swap! text-in #(-> evt .-target .-value)) (syllaby @text-in))) :value @text-in}]
     [:pre @text-syllabied]
     [:table.table.table-striped.table-bordered.table-hover.table-condensed.syllabied
      [:tr [:th] (for [{:keys [short name]} models] [:th {:key name :title name} short])]
      (for
        [[word syll-models] @splitted-mapped-atom]
        [:tr {:key (rand)} [:td word]
         (map-indexed (fn [ix word] [:td {:key ix} word]) syll-models)
         ]
        )
      ]
     ]
    )
  )

(defn table-text []
  [:div]
  )

(defn check-yourself []
  [:div
   [:h2.ribbon "Your turn!"]
   [:h4 "Here you can check yourself: Which model follows your intution?"]
   [:p.well.bg-warning
    "Set the division with a dash '-': 'игорь' -> 'и-горь'. There will be 10 words to divide."
    [:strong "Important"]
    [:div.input-group.col-sm-4
     [:input.form-control {:value @current-word, :on-change #(reset! current-word (-> % .-target .-value))}]
     [:div.input-group-btn
      [:button.btn.btn-default
       {:on-click #(next-test)} (if (-> (count @user-history) (= how-many)) "Result" "Next")]]]
    [:ul.list-group
     {:style {:margin-top "10px"}}
     (for [[k v] @user-history]
       [:li.list-group-item {:key k} (str k " -> " v)])]]
   [:table.table
    [:tr
     (for [{:keys [short]} models] [:th {:key short}  short])] [:tr (for [v @result] [:td {:key (rand)} v])]]
   (progress-tab)
   ])


(defn home-page []
  [:div [:h1.header.navbar "Welcome to " [:strong "russyll"] ", the automatic syllable divider"]
   [:div#app-body
    (check-yourself)
    (textarea)
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

(js/setTimeout #(syllaby @text-in) 500)

(defn init! []
  (hook-browser-navigation!)
  (mount-root)

  )

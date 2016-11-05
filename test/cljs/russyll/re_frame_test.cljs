(ns russyll.re-frame-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [clojure.test :as t]
   [re-frame.core :as re-frame]
   [russyll.core :as core]
;   [re-frame.test :as rf-test]
   )
  )
(re-frame/dispatch [:initiliaze])

(deftest app-db
  (testing "App db has the right data model"
    (let [current-word (re-frame/subscribe [:key-in-db :current-word])
          ]
      (is (= [] (:user-history core/app-db)))
      (is (= "Igor" @current-word))
      (re-frame/dispatch-sync [:set-current-word "cool Word"])
      (is (= "cool Word" @current-word))
      (is (= [] (:user-history core/app-db))))
    )
  )

(deftest subscriptions
  (testing "The subscriptions should give the right model back"
    (let [text-in (re-frame/subscribe [:key-in-db :text-in])]
      (is (= core/example-text @text-in)))))


(deftest handlers-user-test
  (testing "The handlers should update the models correctly"
    (let [
          user-history (re-frame/subscribe [:key-in-db :user-history])
          test-cases 15
          user-progress (re-frame/subscribe [:key-in-db :user-progress])
          current-word (re-frame/subscribe [:key-in-db :current-word])
          ]
      test-action (doall (map #(do (re-frame/dispatch-sync [:choose-word]) true) (range test-cases)))

      (is (= (count @user-history) test-cases))
      (is (= (->> @user-history (map #(first %)) (set) (count)) test-cases))

      (re-frame/dispatch-sync [:initiliaze])
      (re-frame/dispatch-sync [:choose-word])
      (re-frame/dispatch-sync [:next-word @current-word])

      (is (= (count @user-history) 1))
      ;; zero because no dash set
      (is (= @user-progress 0))
      (is (= [[@current-word @current-word]] @user-history))
      (let [
            cur-word @current-word
            syllabied-word (str (first @current-word) "-" (subs @current-word 1))]
        (do
          (re-frame/dispatch-sync [:set-current-word syllabied-word])
          (re-frame/dispatch-sync [:next-word])
          (is (= @user-progress 10))
          (is (= [cur-word syllabied-word] (nth @user-history 0)))))
      )))

(def test-text "На вокз'але Никол'аевской жел'езной")
(def test-text-done-sm1 "на во-гз*а-Ле Ни-ко-л*а-е-фской же-Л*ез-ной")
(def test-text-done-sm2 "на во-гз*а-Ле Ни-ко-л*а-е-фской же-Л*е-зной")

(deftest handlers-text-eval
  (testing "The handlers for text syllabification should work"
    (let [
          current-model (re-frame/subscribe [:key-in-db :current-model])
          text-done (re-frame/subscribe [:key-in-db :text-syllabied])]
      (re-frame/dispatch-sync [:initiliaze])
      (re-frame/dispatch-sync [:change-text test-text])
      (re-frame/dispatch-sync [:syllaby])
      (is (= test-text-done-sm1 @text-done))

      (re-frame/dispatch-sync [:change-model 1])
      (re-frame/dispatch-sync [:syllaby])
      (is (= test-text-done-sm2 @text-done))))
  )

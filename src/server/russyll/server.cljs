(ns russyll.server
    (:require [cljs.nodejs :as node]))

(def __dirname (js* "__dirname"))

(def express (node/require "express"))

(def app (new express))

(def static-route (str __dirname "/../../resources/public/index.html"))
(def static-assets(str __dirname "/../../resources/public/"))

(. app (use (. express (static static-assets))))

;; routes get redefined on each reload
(. app (get "/"
            (fn [req res] (. res (sendFile static-route)))))


(def port 5000)

(defn -main
      []
      (.listen app
               port
               (fn []
                   (js/console.log (str "App Started at http://localhost:" port)))))

(set! *main-cli-fn* -main)

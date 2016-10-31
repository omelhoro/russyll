(defproject russyll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring-server "0.4.0"]
                 [reagent "0.6.0"]
                 [reagent-forms "0.5.12"]
                 [reagent-utils "0.1.5"]
                 [ring "1.4.0"]
                 [re-frame "0.8.0"]
                 [ring/ring-defaults "0.1.5"]
                 [prone "0.8.2"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/core.async "0.2.391"]
                 [environ "1.0.1"]
                 [org.clojure/clojurescript "1.9.229" :scope "provided"]
                 [secretary "1.2.3"]
                 ]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-environ "1.0.1"]
            [lein-asset-minifier "0.2.2"]]

  :ring {:handler russyll.handler/app
         :uberwar-name "russyll.war"}

  :min-lein-version "2.5.3"

  :uberjar-name "russyll.jar"

  :main russyll.server

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]

  :minify-assets
  {:assets
    {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs" "src/cljc"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}

                        :server {
                                 :source-paths ["src/server"]
                                 :compiler     {:output-dir    "out"
                                                :output-to     "out/index.js"
                                                :optimizations :none
                                                :source-map    true
                                                :target        :nodejs}

                                 }

                       }}

  :profiles {:dev {:repl-options {:init-ns russyll.repl}

                   :dependencies [
                                  [binaryage/devtools "0.8.2"]
                                  [ring/ring-mock "0.3.0"]
                                  [ring/ring-devel "1.4.0"]
                                  [lein-figwheel "0.4.1"]
                                  [org.clojure/tools.nrepl "0.2.11"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [pjstadig/humane-test-output "0.7.0"]
                                  [cider/cider-nrepl "0.13.0"]]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.7"]
                             [lein-cljsbuild "1.1.4"]
                             [com.cemerick/clojurescript.test "0.3.3"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :nrepl-port 7002
                              :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"
                                                 ]
                              :css-dirs ["resources/public/css"]
                              }

                   :env {:dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:main "russyll.dev"
                                                         :source-map true}}

                                       :test {:source-paths ["src/cljs/russyll"]
                                              :compiler {:output-to "target/test.js"
                                                         :target :nodejs
                                                         :hashbang false
                                                         :optimizations :simple
                                                         :pretty-print true}}
                                        }
                               :test-commands {"unit" ["node" :node-runner "target/test.js"]}

                               }


                   }

             :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                             {:source-paths ["env/prod/cljs"]
                                              :compiler
                                              {:optimizations :advanced
                                               :pretty-print false}}
                                             :server {}
                                            }}}})

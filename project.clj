(defproject russyll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [reagent "0.8.2-SNAPSHOT"]
                 [reagent-utils "0.1.5"]
                 [re-frame "0.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/clojurescript "1.10.339"]
                 [secretary "1.2.3"]
                 ]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-environ "1.0.1"]
            ]

  :min-lein-version "2.5.3"

  :uberjar-name "russyll.jar"

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :cljsbuild {:builds {:app {:source-paths ["src"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}
                       }}

  :profiles {:dev {:repl-options {:init-ns russyll.repl}

                   :dependencies [
                                  [binaryage/devtools "0.8.2"]
                                  [org.clojure/tools.nrepl "0.2.11"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [pjstadig/humane-test-output "0.7.0"]
                                  ]

                   :source-paths ["env/dev/clj"]
                   :plugins [
                            [lein-figwheel "0.5.16"]
                             [lein-doo "0.1.10"]
                             [cider/cider-nrepl "0.13.0"]
                             ]

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

                                       :test {:source-paths ["test/cljs/russyll" "src"]
                                              :compiler {:output-to "target/test.js"
                                                         :output-dir "target/out"
                                                         :main russyll.runner
                                                         :optimizations :none
                                                         }}
                                        }
                               }


                   }

             :uberjar {:hooks [leiningen.cljsbuild]
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

(defproject zombieclj-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av zombieclj.no"
  :url "http://zombieclj.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [optimus "0.15.1"]
                 [stasis "2.2.2"]
                 [ring "1.2.1"]
                 [enlive "1.1.5"]
                 [prone "0.8.0"]
                 [org.clojure/data.xml "0.0.7"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :ring {:handler zombieclj-no.web/app
         :port 3334}
  :aliases {"build-site" ["run" "-m" "zombieclj-no.web/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.8.10"]]}
             :ztdd {:resource-paths ["resources" "zombietdd"]}
             :zclj {:resource-paths ["resources" "zombieclj"]}})

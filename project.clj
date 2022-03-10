(defproject zombieclj-no "0.1.0-SNAPSHOT"
  :description "Statisk generering av zombieclj.no"
  :url "http://zombieclj.no"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [optimus "2022-02-13"]
                 [stasis "2.5.1"]
                 [ring "1.9.5" :exclusions [commons-codec org.clojure/java.classpath]]
                 [enlive "1.1.6"]
                 [prone "2021-04-23"]
                 [org.clojure/data.xml "0.0.8"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :ring {:handler zombieclj-no.web/app
         :port 3334}
  :aliases {"build-site" ["run" "-m" "zombieclj-no.web/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.12.6"]]}
             :ztdd {:resource-paths ["resources" "zombietdd"]}
             :zclj {:resource-paths ["resources" "zombieclj"]}})

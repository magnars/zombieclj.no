(ns zombieclj-no.content
  (:require [clojure.java.io :as io]))

(defn- load-edn [name]
  (read-string (slurp (io/resource name))))

(defn load-content []
  {:seasons (load-edn "episodes.edn")
   :settings (load-edn "settings.edn")
   :mail-signup (slurp (io/resource "mail-signup.html"))
   :disqus-html (slurp (io/resource "disqus.html"))
   :footer (slurp (io/resource "footer.html"))})

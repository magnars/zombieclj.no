(ns zombieclj-no.content
  (:require [clojure.java.io :as io]))

(defn load-content []
  {:seasons (read-string (slurp (io/resource "episodes.edn")))})

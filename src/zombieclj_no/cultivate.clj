(ns zombieclj-no.cultivate)

(defn- add-seasons-info-to-each-episode [season]
  (update-in season [:episodes]
             #(mapv (fn [e] (assoc e :prefixes (:prefixes season))) %)))

(defn cultivate-content [content]
  (update-in content [:seasons]
             #(mapv add-seasons-info-to-each-episode %)))

(ns zombieclj-no.cultivate)

(defn- add-seasons-info-to-each-episode [season]
  (update-in season [:episodes]
             #(mapv (fn [e] (-> e
                                (assoc :prefixes (:prefixes season))
                                (assoc :ditch-disqus? (:ditch-disqus? season)))) %)))

(defn cultivate-content [content]
  (update-in content [:seasons]
             #(mapv add-seasons-info-to-each-episode %)))

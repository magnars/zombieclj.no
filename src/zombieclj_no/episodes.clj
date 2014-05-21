(ns zombieclj-no.episodes)

(defn episode-url [episode]
  (str "/e" (:number episode) ".html"))

(defn video-url [episode]
  (str "http://www.youtube.com/embed/" (:youtube episode) "?hd=1"))

(defn embed-video [episode]
  [:iframe {:src (video-url episode)
            :frameborder 0
            :allowfullscreen true}])

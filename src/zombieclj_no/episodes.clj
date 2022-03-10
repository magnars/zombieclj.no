(ns zombieclj-no.episodes)

(defn episode-url [episode]
  (str "/" (-> episode :prefixes :url) (:number episode) ".html"))

(defn video-url [episode]
  (cond
    (:youtube episode)
    (str "https://www.youtube.com/embed/" (:youtube episode) "?hd=1")

    (:vimeo episode)
    (str "//player.vimeo.com/video/" (:vimeo episode) "?title=0&amp;byline=0&amp;portrait=0")))

(defn embed-video [episode]
  [:iframe {:src (video-url episode)
            :frameborder 0
            :allowfullscreen true}])

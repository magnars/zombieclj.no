(ns zombieclj-no.pages
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [stasis.core :as stasis]
            [zombieclj-no.episodes :refer [episode-url embed-video]]))

(def guests
  {:cjno "Christian Johansen"
   :jhannes "Johannes Brodwall"
   :cia-audience "120 publikummere på CiA"})

(defn- render-episode [{:keys [number name guest upcoming note] :as episode}]
  [:div.episode
   [:a (if upcoming
         {:class "faded"}
         {:href (episode-url episode)})
    (when note
      [:span.note note])
    (when guest
      [:span.note "Starring " (guests guest)])
    (when upcoming
      [:span.note "Kommer " upcoming])
    "Episode " number ": " name]])

(defn render-season* [season]
  [:div.season
   [:h2 (:name season)]
   [:p (:description season)]
   (map render-episode (:episodes season))])

(defn- render-season [season]
  (if (:old? season)
    [:details.old-season
     [:summary (:name season)]
     (render-season* season)]
    (render-season* season)))

(defn index [content]
  {:body (-> (slurp (io/resource "index.html"))
             (str/replace "<episodes/>"
                          (html
                           (map render-season (remove :old? (:seasons content)))
                           (map render-season (filter :old? (:seasons content))))))})

(defn- insert-disqus-thread [html episode]
  (-> html
      (str/replace #":episode-identifier" (or (:disqus-identifier episode)
                                              (str "episode" (-> episode :prefixes :disqus) (:number episode))))
      (str/replace #":episode-link" (episode-url episode))))

(defn- episode-page [episode next-episode content]
  (let [settings (:settings content)
        filename (str (:id settings) "_"
                      (-> episode :prefixes :download)
                      (:number episode) "." (-> content :settings :ext))]
    {:body
     (list
      [:p.intro (-> settings :episode-intro) " "
       (if (:first? episode)
         "Her er første episode:"
         (list "Hvis du er ny her, så vil du kanskje "
               [:a {:href "/"} "starte på forsiden"]
               "."))]
      [:div.embed-container (embed-video episode)]
      [:ul.small.mbm.mts
       (when-not next-episode
         [:li "Hvis du har gått glipp av noe, kan du sjekke ut "
          [:a {:href "/"} "alle episodene"]
          " her."])]
      (when next-episode
        [:div.box
         [:a.right {:href "/"} "Alle episoder"]
         "Sett den ferdig? "
         [:span.nowrap "Her er "
          [:a {:href (episode-url next-episode)}
           "Episode " (:number next-episode) ": " (:name next-episode)]]])
      (when-not (:ditch-disqus? episode)
        (insert-disqus-thread (:disqus-html content) episode)))}))

(defn create-episode-pages [content]
  (-> content :seasons
      (assoc-in [0 :episodes 0 :first?] true)
      (->>
       (mapcat :episodes)
       (remove :upcoming)
       (partition-all 2 1)
       (map (fn [[episode next-episode]]
              [(episode-url episode)
               (episode-page episode next-episode content)]))
       (into {}))))

(defn update-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn create-misc-pages []
  (-> (stasis/slurp-resources "pages" #"\.html$")
      (update-vals (fn [html] {:body html}))))

(defn get-pages [content]
  (merge
   {"/" (index content)}
   (create-episode-pages content)
   (create-misc-pages)))

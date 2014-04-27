(ns zombieclj-no.pages
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [mapdown.core :as mapdown]
            [stasis.core :as stasis]))

(def guests
  {:cjno "Christian Johansen"
   :jhannes "Johannes Brodwall"
   :cia-audience "120 publikummere på CiA"})

(defn- render-episode [{:keys [number name guest upcoming]}]
  [:div.episode
   [:a (if upcoming
         {:class "faded"}
         {:href (str "/e" number ".html")})
    (when guest
      [:span.note "Starring " (guests guest)])
    (when upcoming
      [:span.note "Kommer " upcoming])
    "Episode " number ": " name]])

(defn- render-season [season]
  [:div.season
   [:h2 (:name season)]
   [:p (:description season)]
   (map render-episode (:episodes season))])

(defn index [content]
  {:body (-> (slurp (io/resource "index.html"))
             (str/replace "<episodes/>"
                          (html (map render-season (:seasons content))))
             (str/replace "<mail-signup/>"
                          (:mail-signup content)))})

(defn- episode-url [episode]
  (str "/e" (:number episode) ".html"))

(defn- episode-page [settings episode]
  {:body
   (list
    [:p.intro (-> settings :episode-intro) " "
     (if (:first? episode)
       "Her er første episode:"
       (list "Hvis du er ny her, så vil du kanskje "
             [:a {:href "/"} "starte på forsiden"]
             "."))]
    [:iframe {:width 835
              :height 505
              :src "http://www.youtube.com/embed/o5yG9Rs427A?hd=1"
              :frameborder 0
              :allowfullscreen true}]
    (let [filename (str (:id settings) "_" (:number episode) ".mov")]
      [:ul.small
       [:li "Du kan også laste den ned og se på bussen: "
        [:a {:href (str "http://dl.dropbox.com/u/3615058/" (:id settings) "/" filename "?dl=1")}
         filename]
        " (" (:size episode) ")"]]))})


(defn create-episode-pages [content]
  (-> content :seasons
      (assoc-in [0 :episodes 0 :first?] true)
      (->>
       (mapcat :episodes)
       (remove :upcoming)
       (map (juxt episode-url (partial episode-page (:settings content))))
       (into {}))))

(defn get-pages [content]
  (merge
   {"/" (index content)}
   (create-episode-pages content)))

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

(defn- episode-page [episode]
  {:body "Hei"})

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

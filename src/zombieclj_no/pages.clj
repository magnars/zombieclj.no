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

(defn- render-episode [{:keys [number name guest]}]
  [:div.episode
   [:a {:href (str "/e" number ".html")}
    (when guest
      [:span.guest "Starring " (guests guest)])
    "Episode " number ": " name]])

(defn- render-season [season]
  [:div.season
   [:h2 (:name season)]
   [:p (:description season)]
   (map render-episode (:episodes season))])

(defn- insert-mail-signup [html]
  (str/replace html
               "<mail-signup/>"
               (slurp (io/resource "mail-signup.html"))))

(defn index [content]
  {:body (-> (slurp (io/resource "index.html"))
             (str/replace "<episodes/>"
                          (html (map render-season (:seasons content))))
             insert-mail-signup)})

(defn- episode-url [episode]
  (str "/e" (:number episode) ".html"))

(defn- episode-page [episode]
  {:body "Hei"})

(defn create-episode-pages [content]
  (->> (:seasons content)
       (mapcat :episodes)
       (map (juxt episode-url episode-page))
       (into {})))

(defn get-pages [content]
  (merge
   {"/" (index content)}
   (create-episode-pages content)))

(ns zombieclj-no.pages
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [mapdown.core :as mapdown]))

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

(defn index [content]
  {:body (-> (slurp (io/resource "index.html"))
             (str/replace "<episodes/>"
                          (html (map render-season (:seasons content)))))})

(defn get-pages [content]
  {"/" (index content)})

(ns zombieclj-no.pages
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [mapdown.core :as mapdown]))

(defn- render-episode [episode]
  [:div.episode
   [:a {:href "#"}
    "Episode " (:number episode) ": " (:name episode)]])

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

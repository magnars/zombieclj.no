(ns zombieclj-no.layout
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.page :refer [html5]]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(def settings (read-string (slurp (io/resource "settings.edn"))))

(defn- head-title [page]
  (if-let [title (:title page)]
    (str title " | " (:title settings))
    (:title settings)))

(defn render-page [page]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href "/styles/responsive.css"}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href "/styles/unresponsive.css"}])
    [:title (head-title page)]]
   [:body
    [:script (slurp (io/resource "public/scripts/ga.js"))]
    [:div.main
     [:div.header
      [:img.logo {:src "/img/logo.png"}]
      [:p.teaser (:teaser settings)]]
     [:div.body
      (:body page)]]]))

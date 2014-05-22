(ns zombieclj-no.rss
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [optimus.assets.creation :refer [last-modified]]
            [zombieclj-no.episodes :refer [episode-url video-url]])
  (:import java.text.SimpleDateFormat
           java.util.Date))

(defn to-id-str [str]
  "Replaces all special characters with dashes, avoiding leading,
   trailing and double dashes."
  (-> (.toLowerCase str)
      (str/replace #"[^a-zA-Z0-9]+" "-")
      (str/replace #"-$" "")
      (str/replace #"^-" "")))

(defn- entry [episode]
  [:entry
   [:title (:name episode)]
   [:updated (str (:date episode) "T00:00:00+02:00")]
   [:author [:name "Magnar Sveen"]]
   [:author [:name "Christian Johansen"]]
   [:link {:href (episode-url episode)}]
   [:id (str "urn:zombieclj-no:feed:episode:" (to-id-str (:name episode)))]
   [:media:content {:url (video-url episode)
                    :type "application/x-shockwave-flash"
                    :isDefault "true"
                    :expression "full"
                    :medium "video"
                    :lang "no"}]
   [:content {:type "html"} (html [:a {:href (video-url episode)}
                                   "Se videoen"])]])

(defn atom-xml [seasons]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"
            :xmlns:media "http://search.yahoo.com/mrss/"}
     [:id "urn:zombieclj-no:feed"]
     [:updated
      (.format (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX")
               (Date. (last-modified (io/resource "episodes.edn"))))]
     [:title {:type "text"} "ZombieCLJ"]
     [:link {:rel "self" :href "http://www.zombieclj.no/atom.xml"}]
     (->> seasons
          (mapcat :episodes)
          (remove :upcoming)
          reverse
          (map entry))])))

(ns zombieclj-no.web
  (:require [net.cgrand.enlive-html :refer [sniptest]]
            [optimus.assets :as assets]
            [optimus.assets.load-css :refer [external-url?]]
            [optimus.export]
            [optimus.link :as link]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis]
            [zombieclj-no.content :refer [load-content]]
            [zombieclj-no.layout :refer [render-page]]
            [zombieclj-no.pages :as pages]))

(defn get-assets []
  (assets/load-assets "public" ["/styles/responsive.css"
                                #"/img/.+\..+"]))

(defn- optimize-path-fn [request]
  (fn [src]
    (if (external-url? src)
      src
      (or (not-empty (link/file-path request src))
          (throw (Exception. (str "Asset not loaded: " src)))))))

(defn- use-optimized-assets [html request]
  (sniptest html
            [:img] #(update-in % [:attrs :src] (optimize-path-fn request))
            [:link] #(update-in % [:attrs :href] (optimize-path-fn request))))

(defn- prepare-page [page content request]
  (-> page
      (render-page (:settings content))
      (use-optimized-assets request)))

(defn update-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn get-pages []
  (let [content (load-content)]
    (-> (pages/get-pages content)
        (update-vals #(partial prepare-page % content)))))

(def optimize optimizations/all)

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-directory "./build")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-directory)
    (optimus.export/save-assets assets export-directory)
    (stasis/export-pages (get-pages) export-directory {:optimus-assets assets})))

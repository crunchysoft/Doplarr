(ns doplarr.readarr
  (:require
   [config.core :refer [env]]
   [doplarr.arr-utils :as utils]
   [clojure.core.async :as a]))

(def base-url (delay (str (:readarr-url env) "/api/v1")))
(def api-key  (delay (:readarr-api env)))
(def rootfolder (delay (utils/rootfolder @base-url @api-key)))

(defn GET [endpoint & [params]]
  (utils/http-request
   :get
   (str @base-url endpoint)
   @api-key
   params))

(defn POST [endpoint & [params]]
  (utils/http-request
   :post
   (str @base-url endpoint)
   @api-key
   params))

(defn search [search-term]
  (let [chan (a/promise-chan)]
    (a/pipeline
     1
     chan
     (map :body)
     (GET "/book/lookup" {:query-params {:term search-term}}))
    chan))

(defn quality-profiles []
  (let [chan (a/promise-chan)]
    (a/pipeline
     1
     chan
     (map (comp (partial map utils/quality-profile-data) :body))
     (GET "/qualityProfile"))
    chan))

(defn request [book & {:keys [profile-id]}]
  (a/go
    (POST
      "/book"
      {:form-params (merge book
                           {:qualityProfileId profile-id
                            :monitored true
                            :minimumAvailability "announced"
                            :rootFolderPath (a/<! @rootfolder)
                            :addOptions {:searchForBook true}})
       :content-type :json}))
  nil)

(ns clj-graphql.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [com.walmartlabs.lacinia :refer [execute]]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as schema]
            [clojure.java.io :as io]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]))



(defn get-hero [context arguments value]
  (let [{:keys [episode]} arguments]
    (if (= episode :NEWHOPE)
      {:id 1000
       :name "Luke"
       :home-planet "Tatooine"
       :appears-in ["NEWHOPE" "EMPIRE" "JEDI"]}
      {:id 2000
       :name "Lando Calrissian"
       :home-planet "Socorro"
       :appears-in ["EMPIRE" "JEDI"]})))

(def star-wars-schema
  (-> (io/resource "schema.edn")
      slurp
      edn/read-string
      (attach-resolvers {:get-hero get-hero
                         :get-droid (constantly {})})
      schema/compile))


(defn handle-graphql [schema request] 
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (let [query (get-in request [:body :query])
               variables (get-in request [:body :variables])
               operation-name (get-in request [:body :operationName])]
           (execute schema query variables nil {:operation-name operation-name}))})


(defroutes app-routes
  (GET "/" [] "Hello World!!!")
  (ANY "/graphql" request (handle-graphql star-wars-schema request))
  (route/not-found "Not Found"))



(def app
  (-> app-routes
      (wrap-defaults api-defaults)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

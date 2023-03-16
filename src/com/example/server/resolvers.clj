(ns com.example.server.resolvers
  (:require [com.example.server.db-queries :as db-queries]
            [com.wsscode.pathom3.connect.operation :as pco]))

#_(pco/defresolver i-count
    [{:keys [conn] :as _env} _]
    {::pco/input  []
     ::pco/output [:i-count]}
    {:i-count (db-queries/my-test-count conn)})


#_(pco/defmutation create-random-thing [env {:keys [tmpid] :as params}]
  ;; Fake generating a new server-side entity with
  ;; a server-decided actual ID
  ;; NOTE: To match with the Fulcro-sent mutation, we
  ;; need to explicitly name it to use the same symbol
    {::pco/op-name 'com.example.client.mutations/create-random-thing
   ;::pco/params [:tempid]
     ::pco/output [:tempids]}
    (println "SERVER: Simulate creating a new thing with real DB id 123" tmpid)
    {:tempids {tmpid 123}})

#_(pco/defresolver recipe-ingredients
    [{:keys [conn] :as _env} _]
    {:all-recipes [{:id 1
                    :name "spicy salad"
                    :ingredients [{:recipe_id 1
                                   :ingredient_id 1
                                   :qty 10}
                                  {:recipe_id 1
                                   :ingredient_id 2
                                   :qty 10}
                                  {:recipe_id 1
                                   :ingredient_id 3
                                   :qty 10}]}]})

(pco/defresolver all-recipes
  [{:keys [conn] :as _env} _]
  {:all-recipes [#:recipes{:id 1
                           :name "spicy salad"
                           :ingredients [#:ingredients{:id 1
                                                       :name "salt"
                                                       :qty 10}
                                         #:ingredients{:id 2
                                                       :name "pepper"
                                                       :qty 10}
                                         #:ingredients{:id 3
                                                       :name "olive oil"
                                                       :qty 10}]}]})



(def resolvers [all-recipes])
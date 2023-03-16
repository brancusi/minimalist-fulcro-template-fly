(ns com.example.client.ui
  (:require
   [com.example.client.mutations :as mut]
   [com.fulcrologic.fulcro.algorithms.merge :as merge]
   [com.fulcrologic.fulcro.algorithms.tempid :as tempid]
   [com.fulcrologic.fulcro.algorithms.data-targeting :as targeting]
   [com.fulcrologic.fulcro.algorithms.normalized-state :as norm]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc transact!]]
   [com.fulcrologic.fulcro.raw.components :as rc]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [com.fulcrologic.fulcro.dom :as dom :refer [button div form h1 h2 h3 input label li ol p ul]]))

(defsc Ingredient [this {:ingredients/keys [id name qty]}]
  {:ident :ingredients/id
   :query [:ingredients/id :ingredients/name :ingredients/qty]}
  (div
   (p (str name " - " qty))))

(def ui-ingredient (comp/factory Ingredient {:keyfn :ingredients/id}))

(defsc Recipe [this {:recipes/keys [id name ingredients]}]
  {:ident :recipes/id
   :query [:recipes/name :recipes/id {:recipes/ingredients (comp/get-query Ingredient)}]}
  (div
   (p name)
   (ul
    (mapv ui-ingredient ingredients))))

(def ui-recipe (comp/factory Recipe {:keyfn :recipes/id}))

(defsc Root [this {:keys [all-recipes]}]
  {:query [{:all-recipes (comp/get-query Recipe)}]}
  (div
   (ui-recipe (first all-recipes))))

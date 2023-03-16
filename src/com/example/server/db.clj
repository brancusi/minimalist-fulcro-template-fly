(ns com.example.server.db
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [com.example.server.config :as config]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]))

(def ds (jdbc/get-datasource config/db-url))

;; (defn get-connection []
;;   (jdbc/get-connection ds))


#_(def hello (memoize (fn [] (+ 1 1))))

;; (hello)

(defn get-connection* []
  (jdbc/get-connection ds))

(def get-connection (memoize get-connection*))

(defn init-db []
  (let  [conn (get-connection)
         stmts (-> (slurp (io/resource "db-init.sql"))
                   (str/split #"\n----\n"))]
    (assert (>= (count stmts) 2) (str "Expected min 2 migr. stmts, got " (count stmts)))
    ;; Set up the migration support
    (run! #(jdbc/execute-one! conn [%]) (take 2 stmts))
    ;; Run normal migrations
    (->> (drop 2 stmts)
         (map-indexed (fn [i stmt]
                        (println "Maybe executing db-init statement" i "(+ 2)")
                        (jdbc/execute-one! conn [(format "do $do$ begin perform idempotent('%d', $$\n%s\n$$); end $do$;"
                                                         i
                                                         stmt)])))
         doall)))

(defn seed
  []
  (sql/insert-multi! (get-connection) :ingredients
                     [{:id 1
                       :name "salt"}
                      {:id 2
                       :name "pepper"}
                      {:id 3
                       :name "olive oil"}])

  (sql/insert-multi! (get-connection) :recipes
                     [{:id 1
                       :name "spicy salad"}])

  (sql/insert-multi! (get-connection) :recipe_ingredients
                     [{:recipe_id 1
                       :ingredient_id 1
                       :qty 10}
                      {:recipe_id 1
                       :ingredient_id 2
                       :qty 10}
                      {:recipe_id 1
                       :ingredient_id 3
                       :qty 10}]))
;; => #'com.example.server.db/seed


(comment

  (seed)
  (init-db)

;;Keep from folding
  )
(ns koans.koan-dao
  (:require [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]
            [clojure.data.json :as json]))



(def s3 (aws/client {:api :s3
                     :credentials-provider (credentials/basic-credentials-provider
                                            {:access-key-id     "AKIA6MF4K3HFAQOBWK53"
                                             :secret-access-key "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"})}))

(def location {:Bucket "koans-alexa-jayeshb" :Name "koans.json"})

(defn- get-koans-s3 []
  ;;(println "hello")
  ;;(println  (:Body (aws/invoke s3 {:op :GetObject :request {:Bucket (:Bucket location)  :Key (:Name location)}})))
  (json/read-str (slurp (:Body (aws/invoke s3 {:op :GetObject :request {:Bucket (str (:Bucket location))  :Key (str (:Name location))}})))))


(defn getKoan []
  (let [koans (get-koans-s3)]
    (nth koans (rand-int (count koans)))))

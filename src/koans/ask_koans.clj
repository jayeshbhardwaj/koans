(ns koans.ask-koans
  (:gen-class
   :name koans.askLambda
   :methods [[handleLambda [java.util.Map] java.util.Map]])
  (:require [koans.koan-dao :as dao]
            [koans.utils :as utils]))


(defmulti handle-intent
  (fn [req] (-> req utils/extract-intent-name keyword)))

(defmethod handle-intent :AMAZON.StopIntent [request]
  ["Okay, Stopping." true])

(defmethod handle-intent :AMAZON.CancelIntent [request]
  ["Okay, Cancelling." true])


(defmethod handle-intent :AMAZON.HelpIntent [_]
  [utils/help-text false])

(defmethod handle-intent :default [_]
  [utils/help-text false])


(defmethod handle-intent :AskKoanIntent [request]
  [(str "Here is a zen koan for you. " (dao/getKoan)) false])


(defn -handleLambda
  "Entry point for Amazon Lambda call triggered by Alexa.
  Alexa passes in its input as json (transparently parsed to a Map).
  The expected output is Alexa response in the form of a Map (which too will be converted to json by Jackson under the hood)."
  [obj input]
  (apply utils/create-simple-response  (handle-intent input)))

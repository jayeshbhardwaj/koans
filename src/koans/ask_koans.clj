(ns koans.ask-koans
  (:gen-class
   :name koans.askLambda
   :methods [[handleLambda [java.util.Map] java.util.Map]]))


(defn create-simple-response
  "Creates a skill response with the given title and text. Optionally closes session.
  The response specifies output speech and card."
  [text shouldEndSession]
  {"version"           "1.0"
   "sessionAttributes" {}
   "response"          {"outputSpeech"     {"type" "PlainText"
                                            "text" text}

                        "shouldEndSession" shouldEndSession}})


(def help-text "Say Koans to get started.")
(def total 3)
(def zen-koans {1 "Nan-in, a Japanese master during the Meiji era (1868-1912), received a university professor who came to inquire about Zen.Nan-in served tea. He poured his visitor's cup full, and then kept on pouring. The professor watched the overflow until he no longer could restrain himself. It is overfull. No more will go in! Like this cup, Nan-in said, you are full of your own opinions and speculations. How can I show you Zen unless you first empty your cup? "
                2 "The master Bankei's talks were attended not only by Zen students but by persons of all ranks and sects. He never quoted sutras not indulged in scholastic dissertations. Instead, his words were spoken directly from his heart to the hearts of his listeners. His large audience angered a priest of the Nichiren sect because the adherents had left to hear about Zen. The self-centered Nichiren priest came to the temple, determined to have a debate with Bankei. Hey, Zen teacher! he called out. Wait a minute. Whoever respects you will obey what you say, but a man like myself does not respect you. Can you make me obey you? Come up beside me and I will show you, said Bankei. Proudly the priest pushed his way through the crowd to the teacher. Bankei smiled. Come over to my left side. The priest obeyed. No, said Bankei, we may talk better if you are on the right side. Step over here. The priest proudly stepped over to the right. You see, observed Bankei, you are obeying me and I think you are a very gentle person. Now sit down and listen."
                3 "The Zen master Hakuin was praised by his neighbours as one living a pure life. A beautiful Japanese girl whose parents owned a food store lived near him. Suddenly, without any warning, her parents discovered she was with child. This made her parents angry. She would not confess who the man was, but after much harassment at last named Hakuin. In great anger the parent went to the master. 'Is that so?' was all he would say. After the child was born it was brought to Hakuin. By this time he had lost his reputation, which did not trouble him, but he took very good care of the child. He obtained milk from his neighbours and everything else he needed. A year later the girl-mother could stand it no longer. She told her parents the truth - the real father of the child was a young man who worked in the fishmarket. The mother and father of the girl at once went to Hakuin to ask forgiveness, to apologize at length, and to get the child back. Hakuin was willing. In yielding the child, all he said was: 'Is that so?' "


                })

(defn extract-intent-name
  "Extracts intent name from the given skill request structure."
  [request & not-found]
  (get-in request ["request" "intent" "name"] not-found))

(defmulti handle-intent
  (fn [req] (-> req extract-intent-name keyword)))

(defmethod handle-intent :AMAZON.StopIntent [request]
  ["Okay, Stopping." true])

(defmethod handle-intent :AMAZON.CancelIntent [request]
  ["Okay, Cancelling." true])


(defmethod handle-intent :AMAZON.HelpIntent [_]
  [help-text false])

(defmethod handle-intent :default [_]
  [help-text false])


(defmethod handle-intent :AskKoanIntent [request]
  [(get zen-koans (inc (rand-int total))) false])


(defn -handleLambda
  "Entry point for Amazon Lambda call triggered by Alexa.
  Alexa passes in its input as json (transparently parsed to a Map).
  The expected output is Alexa response in the form of a Map (which too will be converted to json by Jackson under the hood)."
  [obj input]
  (apply create-simple-response  (handle-intent input)))
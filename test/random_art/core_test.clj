(ns random-art.core-test
  (:require [midje.sweet :refer [facts]]
            [random-art.core :as c]))

(facts
  "Check that a blank array can be created"
  (c/create-canvas 2 4) => [nil nil nil nil nil nil nil nil]
  )

(facts
 "Check that a coordinate can be converted into an array pos"
 (c/coord-to-index 3 2 2) => 8
 (c/coord-to-index 3 0 0) => 0
 (c/coord-to-index 3 1 1) => 4
 )

(facts
 "Check that an array pos can be converted into a coord"
 (c/index-to-coord 3 8) => [2 2]
 (c/index-to-coord 3 0) => [0 0]
 (c/index-to-coord 3 4) => [1 1]
 )

(facts
 "Check that an order to colour the pixels can be retrieved"
 (c/choose-order :linear-horizontal 3 4) => [0 1 2 3 4 5 6 7 8 9 10 11]
 )

(facts
 "Check that a collection of colours can be summed"
 (c/sum-colours [[20 78 100] [40 21 50] [37 8 60]]) => [97 107 210]
 )

(facts
 "Check that a colour can be divided through"
 (c/divide-colour [300 620 450] 10) => [30 62 45]
 (c/divide-colour [10 30 18] 4) => [2 7 4]
 )

(facts
 "Check that a collection of colours can be averaged"
 (c/average-colours [[200 50 60] [255 0 50] [0 100 25]]) => [151 50 45]
 )

(facts
 "Check that a collection of coordinates for a rectangle of size w h at pos x y can be returned"
 (c/get-coordinates 2 3 0 0) => [[0 0] [1 0] [0 1] [1 1] [0 2] [1 2]]
 (c/get-coordinates 2 2 -1 -2) => [[-1 -2] [0 -2] [-1 -1] [0 -1]]
 )

(facts
 "Check that it can be verified that x and y components of coord are im rect w h"
 (c/in-range? [0 0]   [0 0] [1 1]) => true
 (c/in-range? [-1 2]  [0 0] [1 1]) => false
 (c/in-range? [-1 -1] [0 0] [1 1]) => false
 (c/in-range? [1 1] [2 2]) => true
 (c/in-range? [3 3] [2 2]) => false
 )

(facts
 "Check that all the neighbours of a pixel can be retrieved from a rectangle"
 (c/get-neighbours [1 1] [0 0] [2 2]) => [[0 0] [1 0] [2 0] [0 1] [2 1] [0 2] [1 2] [2 2]]
 (c/get-neighbours [0 0] [0 0] [2 2]) => [[1 0] [0 1] [1 1]]
 )

(facts
 "Check that a component can be modified and stays within the range 0-255"
 (c/modify-component 60 80) => 140
 (c/modify-component 0 -10) => 10
 (c/modify-component 255 10) => 245
 )

(facts
 "Check that a colour can be modified with a vector of component alterations"
 (c/modify-colour [100 0 0] [10 5 20]) => [110 5 20]
 (c/modify-colour [100 50 40] [-10 -20 -30]) => [90 30 10]
 (c/modify-colour [255 245 235] [10 20 30]) => [245 225 205]
 )

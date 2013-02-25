(ns random-art.core
  (:use [quil.core]))

(defn create-canvas
  "creates an empty array of size w*h with nil values"
  [w h] (vec (repeat (* w h) nil)))

(defn coord-to-index
  "Converts an x y coord in a rect of with w into an index in an array"
  [w coord] (+ (* w (coord 1)) (coord 0)))

(defn index-to-coord
  "Converts an index within an array to a coordinate within a rectangle of width w"
  [w i] [(rem i w) (quot i w)])

(defn- horiz-linear-order [w h]
  (range 0 (* w h)))

(defn choose-order
  "Returns an order of indices to colour the pixels in"
  [type w h]
  (case type
    :linear-horizontal (horiz-linear-order w h)
    (throw (Exception. "Unknown Order"))
    ))

(defn sum-colours
  "Sums a collection of colours"
  [colours]
  (reduce (fn [a b] (map + a b)) colours))

(defn divide-colour
  "Divide each component of a colour by n"
  [colour n]
  (map #(int (/ % n)) colour))

(defn average-colours
  "Returns the average colour of a collection of colours"
  [colours]
  (divide-colour (sum-colours colours) (count colours)))

(defn get-coordinates
  "Returns a collection of coords for rect with dimensions w h and starting corner at x y"
  ([w h x y] (
    map
    (fn [n] [(+ (rem n w) x) (+ (quot n w) y)])
    (range 0 (* w h))))
  ([w h] (get-coordinates w h 0 0)))

(defn in-range?
  "Checks that x and y coord components are positive"
  ([coord st-coord end-coord]
     (and (>= (coord 0) (st-coord 0))
          (>= (coord 1) (st-coord 1))
          (<= (coord 0) (end-coord 0))
          (<= (coord 1) (end-coord 1)))
     )
  ([coord end-coord]
     (in-range? coord [0 0] end-coord)))



(defn get-neighbours [coord st-coord end-coord]
  (let [coords (vec (get-coordinates 3 3 (dec (coord 0)) (dec (coord 1))))]
    (vec (filter #(in-range? % st-coord end-coord) (concat (subvec coords 0 4)
                                                           (subvec coords 5 9))))))



(defn- random-colour [] [(rand-int 255) (rand-int 255) (rand-int 255)])

(defn- choose-random-colour [pixels w h coord] (random-colour))

(defn modify-component [c d]
  (let [r (+ c d)]
    (cond
     (< r 0)   (Math/abs r)
     (> r 255) (- r (* 2 d))
     :else r)))

(defn modify-component2 [c d]
  (/ (mod (* (+ c d) 2) 510) 2))

(defn modify-colour [colour deviation]
  (vec (for [i (range 0 3)] (modify-component (nth colour i) (nth deviation i)))))

(defn get-pixels [pixels w coords]
  (map #(pixels (coord-to-index w %)) coords))

(defn get-filled-pixels [pixels w coords]
  (filter #(not (nil? %)) (get-pixels pixels w coords)))

(defn print-pixels [tr-pixels w]
  (println (map-indexed (fn [i v] [(index-to-coord w i) v])
                        (for [ind (range 0 (count tr-pixels))]
                          (tr-pixels ind)))))

(defn rint [upper-limit]
  (- (rand-int (* 2 upper-limit)) upper-limit))

(take 40 (repeatedly #(rint 40)))

(defn choose-averaged-colour [pixels w h coord]
  (let [neighbours (get-neighbours coord [0 0] [(dec w) (dec h)])
        colours (get-filled-pixels pixels w neighbours)]
    (if (empty? colours)
      [255 0 0]
      (modify-colour (average-colours colours) [(rint 10) (rint 10) (rint 10)]))))

(defn- fill-canvas [w h]
  (println "FILLING CANVAS")
  (let [pixels (transient (create-canvas w h))]
    (doseq [p (choose-order :linear-horizontal w h)]
      (let [coord (index-to-coord w p)]
        (assoc! pixels p (choose-averaged-colour pixels w h coord))))
    (persistent! pixels))
  (println "FINISHED CANVAS"))

(defn setup []
  (smooth)
  (background 100 0 0)
  (let [art-width 600 art-height 400 pixel-width 1]
    (let [art (fill-canvas art-width art-height)]
      (doseq [y (range 0 art-height)]
        (doseq [x (range 0 art-width)]
          (let [colour (art (index art-width x y))]
            (fill (colour 0) (colour 1) (colour 2))
            (stroke-weight 0)
            (rect (* pixel-width x) ( * pixel-width y) pixel-width pixel-width)))))))

(defn make []
  (defsketch example
    :title "Basic Canvas"
    :setup setup
    :size [600 400]))

(make)


(let [v (transient [0 0 0 0])]
  (assoc! v 2 3)
  (println (for [i (range 0 (count v))] (v i)))
  (persistent! v)
  )

(ns usertest_test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   )
  (:use
   [russyll.usertest :only [calc-stats]
    ]
    ))

(def words {"подмоск*овный" "по-дмо-ск*о-вный" ;[0 1 1 0 0]
            "волш*ебный" "вол-ш*еб-ный" ;[0 0 0 1 0]
            "контр*актный" "кон-тр*акт-ный" ;[0 0 0 1 0]
            "карт*ошка"  "кар-т*ош-ка" ;[0 0 0 0 1]
            "конц*епция" "кон-ц*еп-ци-я" ;[0 0 0 1 0]
            "к*арточка" "к*ар-точ-ка" ;[0 0 0 0 1]
            "комф*ортно" "ком-ф*орт-но" ;[0 0 0 1 0]
            "нарком*анка" "на-рко-м*ан-ка" ;[1 0 0 0 1]
            }
  )
(deftest stats-t
  (testing "The correctness of stats for hashmaps"
    (do (is (= [1 1 1 4 2] (calc-stats words))))
    )

  (testing "The correctness of stats for vectors"
    (do (is (= [1 1 1 4 2] (calc-stats (vec words))))))

  (testing "The correctness of stats for empty sets should be zero"
    (do (is (= [0 0 0 0 0] (calc-stats (vec {}))))))
  )

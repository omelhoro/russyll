(ns russyll.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [usertest_test]
              [syllab-test]
              [orphoep_test]
              [text-test]
              [russyll.re-frame-test]))

(doo-tests 'usertest_test
           'syllab-test
           'orphoep_test
           'text-test
           'russyll.re-frame-test)

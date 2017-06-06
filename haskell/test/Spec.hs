{-# LANGUAGE OverloadedStrings #-}

import Test.Tasty
import CacheSpec

main :: IO ()
main =
  defaultMain (testGroup "Tests" [test_cache])

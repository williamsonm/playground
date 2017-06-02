{-# LANGUAGE OverloadedStrings #-}

import Test.Tasty
import CacheSpec

main :: IO ()
main =
  defaultMain (testGroup "Cache Tests" [shortTests, longTests])

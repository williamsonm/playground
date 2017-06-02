{-# LANGUAGE OverloadedStrings #-}

module CacheSpec (shortTests, longTests) where

import Control.Lens
import Data.ByteString hiding (map, pack)
import Data.ByteString.Char8 (pack)
import Network.Wreq
import Test.Tasty
import Test.Tasty.HUnit

hosts :: [String]
hosts = [ "http://devaempub01.pennwell.net"
        , "http://devaempub02.pennwell.net"
        ]
short :: [String]
short = [ "/etc/designs/corporate_design/static/clientlibs.js"
        , "/etc/clientlibs/pwfe-search.js"
        ]
long :: [String]
long = [ "/etc/clientlibs/foundation/main.js"
       , "/etc/clientlibs/granite/jquery.js"
       ]

fetchCacheHeader :: String -> IO ByteString
fetchCacheHeader url = do
  resp <- getWith opts url
  return (resp ^. responseHeader "Cache-Control")
  where
    opts = defaults & header "Server-Agent" .~ ["Communique-Dispatcher"]

mkResourceTests :: Int -> [String] -> [TestTree]
mkResourceTests maxAge paths =
  map (mkResourceTest maxAge) [host ++ path | path <- paths, host <- hosts]

mkResourceTest :: Int -> String -> TestTree
mkResourceTest maxAge url =
  withResource acquire release test
  where
    acquire = fetchCacheHeader url
    release _ = return ()
    expected = pack ("max-age=" ++ show maxAge)

    test getResource =
      testCase url $
        getResource >>= assertEqual url expected

shortTests :: TestTree
shortTests = testGroup "Max-Age 900" (mkResourceTests 900 short)

longTests :: TestTree
longTests = testGroup "Max-Age 604800" (mkResourceTests 604800 long)

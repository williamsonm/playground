{-# LANGUAGE OverloadedStrings #-}

module CacheSpec where

import Control.Lens
import Data.ByteString hiding (map, pack)
import Data.ByteString.Char8 (pack)
import Network.Wreq
import Test.Tasty
import Test.Tasty.HUnit

type Path = String
newtype Host = Host String
newtype MaxAge = MaxAge Int

instance Show Host where
  show (Host host) = "Host: " ++ host

instance Show MaxAge where
  show (MaxAge maxAge) = "Max-Age: " ++ show maxAge

pwMaxAge, aemMaxAge :: MaxAge
pwMaxAge  = MaxAge 2592000 -- 900 -- 86400
aemMaxAge = MaxAge 2592000 -- 604800

fetchCacheHeader :: String -> IO ByteString
fetchCacheHeader url =
  view (responseHeader "Cache-Control") <$> getWith opts url
  where
    opts = defaults & header "Server-Agent" .~ ["Communique-Dispatcher"]

mkResourceTest :: Host -> MaxAge -> Path -> TestTree
mkResourceTest (Host host) (MaxAge maxAge) path =
  withResource acquire release $ \getResource ->
    testCase path $
      getResource >>= assertEqual url expected
  where
    url = host ++ path
    acquire = fetchCacheHeader url
    release _ = return ()
    expected = pack ("max-age=" ++ show maxAge)

mkHostTests :: Host -> TestTree
mkHostTests host =
  testGroup (show host)
    [ testGroup (show pwMaxAge) (map (mkResourceTest host pwMaxAge) short)
    , testGroup (show aemMaxAge) (map (mkResourceTest host aemMaxAge) long)
    ]
  where
    short :: [Path]
    short = [ "/etc/designs/corporate_design/static/clientlibs.js"
            , "/etc/designs/ogj2_design/static.css"
            , "/etc/designs/corporate_design.css"
            , "/etc/clientlibs/pwfe-search.js"
            , "/etc/designs/ogj2_design.css"
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASH3b9b3e3d88b9d154462bed00eacc5eec.js" -- 596aeb2ea2d2f45b0b11301a6681596fc19972f6
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASHb057a2fc916b4238e64d6a4d01da9988.js" -- 259ff8cc10d8130f08b4c1219f1092194d315ac7 -- 15-mins should evaporate
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASH112dd0b7f7af1027c9feef5caaa47aae.js" -- 44dc31517e0617c944741e035e97ef22fe38ff1d
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASH3b9b3e3d88b9d154462bed00eacc5eez.js" -- fails
            ]
    long :: [Path]
    long = [ "/etc/clientlibs/foundation/main.js"
           , "/etc/clientlibs/granite/jquery.js"
           , "/etc/clientlibs/granite/jquery/granite.ACSHASH1cd927e8b915fa4931c6c086a8cfda10.js"
           ]

test_cache :: TestTree
test_cache =
  testGroup "Ensure max-age header is set" $
    map mkHostTests hosts
  where
    hosts = [ Host "http://testaempub01.pennwell.net"
            , Host "http://testaempub02.pennwell.net"
            , Host "http://testaemweb01.pennwell.net"
            , Host "http://testaemweb02.pennwell.net"
            -- , Host "http://aemstatic-ww-test1.azureedge.net"
            -- , Host "http://aemstatic-ww-test2.azureedge.net"
            -- , Host "http://devaempub01.pennwell.net"
            -- , Host "http://devaempub02.pennwell.net"
            -- , Host "http://devaemweb01.pennwell.net"
            -- , Host "http://devaemweb02.pennwell.net"
            ]

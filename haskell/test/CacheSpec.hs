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

pwMaxAge, aemMaxAge, brandMaxAge, fontAge :: MaxAge
pwMaxAge  = MaxAge 2592000 -- 900 -- 86400
aemMaxAge = MaxAge 2592000 -- 604800
brandMaxAge = MaxAge 900
fontAge = MaxAge 31536000

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

mkFontTests :: Host -> TestTree
mkFontTests host =
  testGroup (show host)
    [ testGroup (show fontAge) (map (mkResourceTest host fontAge) fonts) ]
  where
    fonts :: [Path]
    fonts = [ "/etc/designs/corporate_design/fonts/fontawesome-webfont.woff2"
            , "/etc/designs/corporate_design/fonts/fontawesome-webfont.woff"
            , "/etc/designs/corporate_design/fonts/fontawesome-webfont.eot"
            , "/etc/designs/corporate_design/fonts/fontawesome-webfont.ttf"
            ]

mkHostTests :: Host -> TestTree
mkHostTests host =
  testGroup (show host)
    [ testGroup (show pwMaxAge) (map (mkResourceTest host pwMaxAge) short)
    , testGroup (show aemMaxAge) (map (mkResourceTest host aemMaxAge) long)
    , testGroup (show brandMaxAge) (map (mkResourceTest host brandMaxAge) brands)
    ]
  where
    short :: [Path]
    short = [ "/etc/designs/corporate_design/static/clientlibs.ACSHASH305b0e14f7396eca8d68e0a808dbf440.js"
            , "/etc/designs/corporate_design.ACSHASHe638954399f9f29ecfa49da9dac1f022.css"
            , "/etc/clientlibs/pwfe-search.ACSHASH07ba0efee42416f7435a63bdcf47160d.js"
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASH3b9b3e3d88b9d154462bed00eacc5eec.js" -- 596aeb2ea2d2f45b0b11301a6681596fc19972f6
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASHb057a2fc916b4238e64d6a4d01da9988.js" -- 259ff8cc10d8130f08b4c1219f1092194d315ac7 -- 15-mins should evaporate
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASH112dd0b7f7af1027c9feef5caaa47aae.js" -- 44dc31517e0617c944741e035e97ef22fe38ff1d
            -- , "/etc/designs/corporate_design/static/clientlibs.ACSHASH3b9b3e3d88b9d154462bed00eacc5eez.js" -- fails
            ]
    long :: [Path]
    long = [ "/etc/clientlibs/foundation/main.js"
           , "/etc/clientlibs/granite/jquery.js"
          --  , "/etc/clientlibs/granite/jquery/granite.ACSHASH1cd927e8b915fa4931c6c086a8cfda10.js"
           ]
    brands = [ "/etc/designs/ogj2_design.css"
             , "/etc/designs/ogj2_design/static.css"
             , "/etc/designs/corporate_design/content1.css"
             , "/etc/designs/corporate_design/content2.css"
             , "/etc/designs/corporate_design/content3.css"
             , "/etc/designs/corporate_design/content4.css"
             , "/etc/designs/corporate_design/content5.css"
             , "/etc/designs/corporate_design/content6.css"
             , "/etc/designs/corporate_design/content7.css"
             , "/etc/designs/corporate_design/header1.css"
             , "/etc/designs/corporate_design/header2.css"
             , "/etc/designs/corporate_design/header3.css"
             ]

test_fonts :: TestTree
test_fonts =
  testGroup "Ensure max-age header is set on fonts" $
    map mkFontTests hosts
  where
    hosts = [ Host "http://testaemweb01.pennwell.net"
            , Host "http://testaemweb02.pennwell.net"
            , Host "http://aemstatic-ww-test1.azureedge.net"
            , Host "http://aemstatic-ww-test2.azureedge.net"
            , Host "http://devaemweb01.pennwell.net"
            , Host "http://devaemweb02.pennwell.net"
            ]

-- test_cache :: TestTree
-- test_cache =
--   testGroup "Ensure max-age header is set" $
--     map mkHostTests hosts
--   where
    -- hosts = [ Host "http://localhost:4503" ]
    -- hosts = [ Host "http://testaempub01.pennwell.net"
    --         , Host "http://testaempub02.pennwell.net"
            -- , Host "http://testaemweb01.pennwell.net"
            -- , Host "http://testaemweb02.pennwell.net"
            -- , Host "http://aemstatic-ww1.azureedge.net"
            -- , Host "http://aemstatic-ww-test1.azureedge.net"
            -- , Host "http://aemstatic-ww-test2.azureedge.net"
            -- , Host "http://devaempub01.pennwell.net"
            -- , Host "http://devaempub02.pennwell.net"
            -- , Host "http://devaemweb01.pennwell.net"
            -- , Host "http://devaemweb02.pennwell.net"
            -- ]

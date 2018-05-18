module FontSpec where

import Fonts
import Haxl.Core

-- import Test.Tasty
-- import Test.Tasty.HUnit

main :: IO ()
main = do
  userState <- initGlobalState combos
  let stateStore = stateSet userState stateEmpty
  env0 <- initEnv stateStore ()
  ages <- runHaxl env0 (mapM getCacheHeader combos)
  -- cache <- runHaxl env0 dumpCacheAsHaskell
  -- putStrLn cache
  putStrLn "ages."
  accesses <- runHaxl env0 (mapM getAccessHeader combos)
  putStrLn "done."
  mapM_ print (zip ages accesses)
  where
    fonts = [ "/etc/designs/corporate_design/fonts/fontawesome-webfont.woff2"
            , "/etc/designs/corporate_design/fonts/fontawesome-webfont.woff"
            , "/etc/designs/corporate_design/fonts/fontawesome-webfont.eot"
            , "/etc/designs/corporate_design/fonts/fontawesome-webfont.ttf"
            ]
    hosts = [ "http://testaemweb01.pennwell.net"
            , "http://aemstatic-ww-qa1.azureedge.net"
            , "http://aemstatic-ww-qa2.azureedge.net"
            ]

    combos :: [String]
    combos = [a ++ b | a <- hosts, b <- fonts]

-- test_fontz :: TestTree
-- test_fontz =
--   testGroup "Ensure max-age header is set on fonts" $
--     error "wuat"

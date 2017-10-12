{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GADTs #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE StandaloneDeriving #-}
{-# LANGUAGE TypeFamilies #-}

module FontSpec where

import Control.Concurrent.Async
import Control.Lens
import Control.Monad
import qualified Data.ByteString as BS
-- import Data.ByteString.Char8 (pack)
import Data.Hashable
import Data.Typeable
import Haxl.Core
import Network.Wreq
import Network.HTTP.Types

-- import Test.Tasty
-- import Test.Tasty.HUnit

main :: IO ()
main = do
  let stateStore = stateSet UserState{cachedFonts = combos} stateEmpty
  env0 <- initEnv stateStore ()
  ages <- runHaxl env0 (mapM getCacheHeader combos)
  mapM_ print ages
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

type Haxl = GenHaxl ()
type Path = String
data MaxAge = MaxAge String BS.ByteString

instance Show MaxAge where
  show (MaxAge url maxAge) = "Max-Age: " ++ show maxAge ++ " => " ++ url

data CacheReq a where
  GetMaxAge :: String -> CacheReq MaxAge
  deriving (Typeable)

deriving instance Eq (CacheReq a)
instance Hashable (CacheReq a) where
  hashWithSalt s (GetMaxAge age) = hashWithSalt s (0::Int, age)

deriving instance Show (CacheReq a)
instance ShowP CacheReq where showp = show

instance StateKey CacheReq where
  data State CacheReq = UserState { cachedFonts :: [Path] }

instance DataSourceName CacheReq where
  dataSourceName _ = "CacheDataSource"

instance DataSource c CacheReq where
  fetch _state _flags _userEnv blockedFetches = AsyncFetch $ \inner -> do

    asyncs <- mapM fetchHeaderAsync blockedFetches
    inner
    mapM_ wait asyncs

    -- unless (null ids) $ do
    --   asyncs <- map fetchHeaderAsync blockedFetches
    --   inner
    --   mapM_ wait asyncs
      -- allAges <- mapM fetchMaxAge (cachedFonts state)
      -- mapM_ (uncurry putSuccess) (zip vars allAges)

    where
      ids :: [Path]
      vars :: [ResultVar MaxAge]
      (ids, vars) = unzip
        [(age, r) | BlockedFetch (GetMaxAge age) r <- blockedFetches]

getCacheHeader :: String -> Haxl MaxAge
getCacheHeader url =
  dataFetch (GetMaxAge url)

fetchMaxAge :: String -> IO MaxAge
fetchMaxAge url = MaxAge url <$> fetchHeader hCacheControl url

hAccessControlAllowOrigin :: HeaderName
hAccessControlAllowOrigin = "Access-Control-Allow-Origin"

fetchHeaderAsync :: BlockedFetch CacheReq -> IO (Async ())
fetchHeaderAsync (BlockedFetch req rvar) =
  async $ do
    a <- fetchReqz req
    putSuccess rvar a

fetchReqz :: CacheReq a -> IO a
fetchReqz (GetMaxAge url) =
  MaxAge url <$> fetchHeader hAccessControlAllowOrigin url

fetchHeader :: HeaderName -> String -> IO BS.ByteString
fetchHeader h url =
  view (responseHeader h) <$> getWith opts url
  where
    opts = defaults & header "Server-Agent" .~ [ "Communique-Dispatcher" ]

-- test_fontz :: TestTree
-- test_fontz =
--   testGroup "Ensure max-age header is set on fonts" $
--     error "wuat"

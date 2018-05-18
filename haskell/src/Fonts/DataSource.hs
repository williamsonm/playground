{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE GADTs #-}
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE StandaloneDeriving #-}
{-# LANGUAGE TypeFamilies #-}

module Fonts.DataSource where

import Control.Concurrent.Async
import Control.Lens
-- import Control.Monad
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
-- import Data.ByteString.Char8 (pack)
import Data.Hashable
import Data.Typeable
import Haxl.Core
import Network.Wreq
-- import Network.HTTP.Types

type Path = String
data Header = Header String BS.ByteString

instance Show Header where
  show (Header name maxAge) = "Header: " ++ name ++ " => " ++ show maxAge

data CacheReq a where
  GetMaxAge :: String -> CacheReq Header
  GetAccess :: String -> CacheReq Header
  GetResponse :: String -> CacheReq (Response BSL.ByteString)
  deriving (Typeable)

deriving instance Eq (CacheReq a)
instance Hashable (CacheReq a) where
  hashWithSalt s (GetMaxAge url) = hashWithSalt s (0::Int, url)
  hashWithSalt s (GetAccess url) = hashWithSalt s (1::Int, url)
  hashWithSalt s (GetResponse url) = hashWithSalt s (2::Int, url)

deriving instance Show (CacheReq a)
instance ShowP CacheReq where showp = show

instance StateKey CacheReq where
  data State CacheReq = UserState { cachedFonts :: [Path] }

instance DataSourceName CacheReq where
  dataSourceName _ = "CacheDataSource"

instance DataSource c CacheReq where
  fetch = cacheFetch

initGlobalState :: [String] -> IO (State CacheReq)
initGlobalState fonts = return UserState { cachedFonts = fonts }

cacheFetch
  :: State CacheReq
  -> Flags
  -> u
  -> [BlockedFetch CacheReq]
  -> PerformFetch

cacheFetch _state _flags _userEnv blockedFetches =
  AsyncFetch $ \inner -> do
    asyncs <- mapM fetchAsync blockedFetches
    inner
    mapM_ wait asyncs

fetchAsync :: BlockedFetch CacheReq -> IO (Async ())
fetchAsync (BlockedFetch req rvar) = async $
  putSuccess rvar =<< fetchCacheReq req

fetchCacheReq :: CacheReq a -> IO a

fetchCacheReq (GetMaxAge url) = do
  resp <- fetchCacheReq (GetResponse url)
  let age = view (responseHeader "Cache-Control") resp
  return (Header url age)

fetchCacheReq (GetAccess url) = do
  resp <- fetchCacheReq (GetResponse url)
  let access = view (responseHeader "Access-Control-Allow-Origin") resp
  return (Header url access)

fetchCacheReq (GetResponse url) = do
  putStr "."
  getWith opts url
  where
    opts = defaults & header "Server-Agent" .~ [ "Communique-Dispatcher" ]

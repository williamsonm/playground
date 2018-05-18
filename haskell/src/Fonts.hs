{-# LANGUAGE OverloadedStrings #-}

module Fonts
  ( getAccessHeader
  , getCacheHeader
  , initGlobalState
  , Header(..)
  )
  where

import Control.Lens
import qualified Data.ByteString.Lazy as BSL
import Fonts.DataSource
import Haxl.Core
import Network.Wreq

-- caching the full http response is gross
getResponse :: String -> GenHaxl u (Response BSL.ByteString)
getResponse url =
  dataFetch (GetResponse url)

getCacheHeader :: String -> GenHaxl u Header
getCacheHeader url = do
  resp <- getResponse url
  let headerName = "Cache-Control"
      age = view (responseHeader headerName) resp
  return (Header (show headerName) age)
  -- dataFetch (GetMaxAge url)

getAccessHeader :: String -> GenHaxl u Header
getAccessHeader url = do
  resp <- getResponse url
  let headerName = "Access-Control-Allow-Origin"
      access = view (responseHeader headerName) resp
  return (Header (show headerName) access)
  -- dataFetch (GetAccess url)

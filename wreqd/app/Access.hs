{-# LANGUAGE ExtendedDefaultRules #-}
{-# LANGUAGE OverloadedStrings #-}

module Access where

import Control.Applicative
import Data.Aeson

data AccessResponse = AccessSuccess { access :: String }
                    | AccessError { error :: String, status :: Int }
                    deriving (Show)

instance FromJSON AccessResponse where
  parseJSON (Object v) =
    parseSuccess <|> parseError
    where
      parseSuccess = AccessSuccess <$> v .: "access"
      parseError = AccessError <$> v .: "error" <*> v .: "status"

  parseJSON _ = mempty

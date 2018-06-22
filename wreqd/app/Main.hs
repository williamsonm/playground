module Main where

import Access

import Control.Lens
import Data.Aeson
import OpenSSL.Session (context)
import Network.HTTP.Client.OpenSSL
import Network.HTTP.Client (defaultManagerSettings, managerResponseTimeout)
import Network.Wreq

main :: IO ()
main = do
  let opts = defaults & manager .~ Left (opensslManagerSettings context)
                      & manager .~ Left (defaultManagerSettings { managerResponseTimeout = Just 10000 } )

  r <- withOpenSSL $
    getWith opts "https://httpbin.org/get"

  print r


main2 :: IO ()
main2 = do
  check >>= print
  putStrLn "done."

check :: IO (Either String AccessResponse)
check = do
  let oneSecond = 1000000
      timeout = Just (30 * oneSecond)
      url = "http://tulaemservices1.pennwell.net:8125/services/user/authorize?email=mattheww@pennwell.com&accessTypes=ROLE_FEART"
      opts = defaults & manager .~ Left (defaultManagerSettings { managerResponseTimeout = timeout } )
  eitherDecode . view responseBody <$> getWith opts url

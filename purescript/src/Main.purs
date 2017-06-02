module Main where

import Prelude (Unit, (+))
import Control.Monad.Eff (Eff)
import Control.Monad.Eff.Console (CONSOLE, log)

apply :: forall a b. (a -> b) -> a -> b
apply f x = f x

infix 0 apply as $

fz :: Int -> Int
fz x = x + 1

az :: Int
az = fz $ 3

main :: forall e. Eff (console :: CONSOLE | e) Unit
main = do
  log "Hello sailor!"

{-# LANGUAGE NoImplicitPrelude #-}

module SimplePrelude
  ( id
  , const
  , (.)
  , P.undefined
  ) where

import qualified Prelude as P

id :: a -> a
id a = a

const :: a -> b -> a
const a _ = a

(.) :: (b -> c) -> (a -> b) -> a -> c
(f . g) x = f (g x)

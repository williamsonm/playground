{-# LANGUAGE NoImplicitPrelude #-}

module Either where

import Functor

data Either a b = Right b
                | Left a

instance Functor (Either a) where
  fmap f (Right b) = Right (f b)
  fmap _ (Left a) = Left a

instance Applicative (Either a) where
  pure = Right
  ap (Right f) r = fmap f r
  ap (Left  a) _ = Left a

instance Monad (Either a) where
  Right b >>= f = f b
  Left  a >>= _ = Left a

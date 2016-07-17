{-# LANGUAGE NoImplicitPrelude #-}

module Maybe where

import Functor

data Maybe a = Just a
             | Nothing

instance Functor Maybe where
  fmap f (Just a) = Just (f a)
  fmap _ Nothing  = Nothing

instance Applicative Maybe where
  pure = Just

  ap (Just f) (Just a) = Just (f a)
  ap _ _ = Nothing

instance Monad Maybe where
  Just a  >>= f = f a
  Nothing >>= _ = Nothing

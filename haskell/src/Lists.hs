module Lists where

type NonEmpty a = Fix (Compose ((,) a) Maybe)
type List     a = Fix (Compose Maybe ((,) a))

newtype Fix f = Fix {unFix :: f (Fix f)}

newtype Compose f g a = Compose {unCompose :: f (g a)}

instance (Functor f, Functor g) => Functor (Compose f g) where
    fmap f = Compose . fmap (fmap f) . unCompose

ne1, ne2 :: NonEmpty Int
ne1 = Fix (Compose (1, Nothing))
ne2 = Fix (Compose (1, Just (Fix (Compose (2, Nothing)))))

l0, l1, l2 :: List Int
l0 = Fix (Compose Nothing)
l1 = Fix (Compose (Just (1, Fix (Compose Nothing))))
l2 = Fix (Compose (Just (1, Fix (Compose (Just (2, Fix (Compose Nothing)))))))

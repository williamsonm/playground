module AgdaBasics where

data Bool : Set where
  true  : Bool
  false : Bool

not : Bool -> Bool
not true  = false
not false = true

data Nat : Set where
  zero : Nat
  suc  : Nat -> Nat

_+_ : Nat -> Nat -> Nat
zero  + m = m
suc n + m = suc (n + m)

_*_ : Nat -> Nat -> Nat
zero  * m = zero
suc n * m = m + n * m

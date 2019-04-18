let
  config = {
    packageOverrides = pkgs: rec {
      haskellPackages = pkgs.haskellPackages.override {
        overrides = haskellPackagesNew: haskellPackagesOld: rec {

          playground = haskellPackagesNew.callPackage ./default.nix {
            zip = pkgs.libzip;
          };
        };
      };
    };
  };

  pkgs = import <nixpkgs> { inherit config; };

in
  { playground = pkgs.haskellPackages.playground;
  }

# DXFer
Android application for those who want to view DXF files without downloading mobile CAD software that is networked, requires an account, or bloated. Viewing DXF files ought to be more like viewing images! 

## Design
- Single activity
- Recieves `*.dxf` file intent
- Converts the dxf to an SVG for rendering using Kabeja

## Current state
- Displays DXF files based on the support(polylines, lines, points, etc) found in Kabeja's library
- TODO: Zooming, scrolling
- TODO: Test DXF formats (may require Kabeja modifications to add support)
  - DXF Binary (Not supported by Kabeja)
  - DXF ASCII
    - AutoCAD releases: R12 (1992) through R2004 (2003) (Should be majority of DXF...)
    - Other versions?
- TODO: Add metadata overlay to view 

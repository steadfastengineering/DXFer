# DXFer
Android application for those who want to view DXF files without downloading mobile CAD software that is networked, requires an account, or bloated. Viewing DXF files ought to be more like viewing images! 

## Design
- Single activity
- Recieves *.dxf file intent
- Converts the dxf to an SVG for rendering

## Current state
- Displays simple DXF files based on the support(polylines, lines, points, etc) found in Kabeja's library
- TODO: Zooming, scrolling
- TODO: Test all DXF formats (may require Kabeja modifications)
- TODO: Add metadata overlay to view 

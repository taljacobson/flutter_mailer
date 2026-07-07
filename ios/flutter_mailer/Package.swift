// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "flutter_mailer",
    platforms: [
        .iOS("13.0")
    ],
    products: [
        .library(name: "flutter-mailer", targets: ["flutter_mailer"])
    ],
    dependencies: [
        .package(name: "FlutterFramework", path: "../FlutterFramework")
    ],
    targets: [
        .target(
            name: "flutter_mailer",
            dependencies: [
                .product(name: "FlutterFramework", package: "FlutterFramework")
            ],
            cSettings: [
                .headerSearchPath("include/flutter_mailer")
            ]
        )
    ]
)

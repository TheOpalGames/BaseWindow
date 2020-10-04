#ifndef ViewController_h
#define ViewController_h

#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>

#import "main-objc.h"

@interface ViewController : NSViewController

@property(nonatomic) PolyWindowContext *ctx;

-(void) enableVsync;
-(void) enableConstantRefresh;
-(void) disableTimers;

@end

#endif /* ViewController_h */

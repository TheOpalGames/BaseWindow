#ifndef ViewController_h
#define ViewController_h

#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>

@interface ViewController : NSViewController

-(void) setContext:(void *) ctx;

-(void) enableVsync;
-(void) enableConstantRefresh;
-(void) disableTimers;

@end

#endif /* ViewController_h */
